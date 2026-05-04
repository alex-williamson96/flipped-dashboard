import { useState, useEffect, useRef } from 'react'
import { useQueryClient } from '@tanstack/react-query'
import useCourses from '../../../hooks/useCourses'
import useLessons from '../../../hooks/useLessons'
import useSections from '../../../hooks/useSections'
import useDashboardSummary, { DEFAULT_FILTERS, dashboardSummaryQuery } from '../hooks/useDashboardSummary'
import useLessonTimeline, { lessonTimelineQuery } from '../hooks/useLessonTimeline'
import FilterBar from '../components/FilterBar'
import AlertBanner from '../components/AlertBanner'
import StatsBar from '../components/StatsBar'
import EngagementFunnel from '../components/EngagementFunnel'
import StudentTable from '../components/StudentTable'
import LessonTimeline from '../components/LessonTimeline'
import IndividualStudentView from '../components/IndividualStudentView'
import WeakestObjectivesPanel from '../components/WeakestObjectivesPanel'

export default function DashboardPage() {
  const [filters, setFilters] = useState({ ...DEFAULT_FILTERS })
  const [selectedStudentId, setSelectedStudentId] = useState(null)
  const autoSelectCourseRef = useRef(null)
  const queryClient = useQueryClient()

  const { data: courses } = useCourses()
  const { data: lessons } = useLessons(filters.courseId)
  const { data: sections } = useSections(filters.courseId)
  const { data: overviewData, isLoading: overviewLoading } = useDashboardSummary(filters)
  const { data: timelineData, isLoading: timelineLoading } = useLessonTimeline({
    courseId: filters.courseId,
    sectionId: filters.sectionId,
  })

  useEffect(() => {
    if (courses && courses.length > 0 && !filters.courseId) {
      setFilters(f => ({ ...f, courseId: courses[0].id }))
    }
  }, [courses])

  useEffect(() => {
    if (filters.courseId !== autoSelectCourseRef.current && lessons && lessons.length > 0) {
      autoSelectCourseRef.current = filters.courseId
      const active = lessons.find(l => l.isActive) ?? lessons[0]
      setFilters(f => ({ ...f, lessonId: active.id }))
    }
  }, [filters.courseId, lessons])

  useEffect(() => {
    if (!filters.courseId || !sections || !lessons) return
    const sectionIds = [null, ...sections.map(s => s.id)]
    const lessonIds = [null, ...lessons.map(l => l.id)]
    for (const sectionId of sectionIds) {
      queryClient.prefetchQuery(lessonTimelineQuery({ courseId: filters.courseId, sectionId }))
      for (const lessonId of lessonIds) {
        queryClient.prefetchQuery(dashboardSummaryQuery({
          ...DEFAULT_FILTERS,
          courseId: filters.courseId,
          sectionId,
          lessonId,
        }))
      }
    }
  }, [filters.courseId, sections, lessons, queryClient])

  function handleFilterChange(partial) {
    setFilters(f => ({ ...f, ...partial }))
    if ('courseId' in partial) {
      setSelectedStudentId(null)
    }
  }

  return (
    <div className="p-4 max-w-7xl mx-auto space-y-4">
      <FilterBar filters={filters} onChange={handleFilterChange} />
      <AlertBanner summary={overviewData} />
      <StatsBar summary={overviewData} loading={overviewLoading} />
      <EngagementFunnel summary={overviewData} loading={overviewLoading} />
      {selectedStudentId ? (
        <IndividualStudentView
          studentId={selectedStudentId}
          courseId={filters.courseId}
          initialLessonId={filters.lessonId}
          filteredStudents={overviewData?.students ?? []}
          onBack={() => setSelectedStudentId(null)}
          onStudentChange={setSelectedStudentId}
        />
      ) : (
        <>
          <WeakestObjectivesPanel loAccuracy={overviewData?.loAccuracy} />
          <StudentTable
            students={overviewData?.students}
            loading={overviewLoading}
            onRowClick={student => setSelectedStudentId(student.studentId)}
          />
          <LessonTimeline lessons={timelineData} loading={timelineLoading} />
        </>
      )}
    </div>
  )
}
