import CourseFilter from './CourseFilter'
import SectionFilter from './SectionFilter'
import LessonFilter from './LessonFilter'
import CompletionFilter from './CompletionFilter'
import ScoreRangeFilter from './ScoreRangeFilter'
import ConfidenceFilter from './ConfidenceFilter'

function FilterField({ label, children }) {
  return (
    <div className="flex flex-col gap-1">
      <span className="text-sm font-medium">{label}</span>
      {children}
    </div>
  )
}

export default function FilterBar({ filters, onChange }) {
  function handleClear() {
    onChange({
      sectionId: null,
      lessonId: null,
      completionStatus: 'all',
      minQuizScore: null,
      maxQuizScore: null,
      confidenceLevel: null,
    })
  }

  return (
    <div className="card card-bordered bg-base-100 p-4 space-y-3">
      <div className="flex items-center justify-between">
        <span className="text-sm font-semibold">Filters</span>
        <button className="btn btn-ghost btn-xs" onClick={handleClear}>Clear filters</button>
      </div>
      <div className="flex flex-wrap gap-4 items-start">
        <FilterField label="Course">
          <CourseFilter
            value={filters.courseId}
            onChange={courseId => onChange({ courseId, sectionId: null, lessonId: null })}
          />
        </FilterField>
        <FilterField label="Section">
          <SectionFilter
            courseId={filters.courseId}
            value={filters.sectionId}
            onChange={sectionId => onChange({ sectionId, lessonId: null })}
          />
        </FilterField>
        <FilterField label="Lesson">
          <LessonFilter
            courseId={filters.courseId}
            value={filters.lessonId}
            onChange={lessonId => onChange({ lessonId })}
          />
        </FilterField>
        <div className="w-px self-stretch bg-base-300 mx-1" />
        <FilterField label="Completion">
          <CompletionFilter
            value={filters.completionStatus}
            onChange={completionStatus => onChange({ completionStatus })}
          />
        </FilterField>
        <FilterField label="Score below">
          <ScoreRangeFilter
            maxScore={filters.maxQuizScore}
            onChange={maxScore => onChange({ minQuizScore: null, maxQuizScore: maxScore })}
          />
        </FilterField>
        <FilterField label="Confidence">
          <ConfidenceFilter
            value={filters.confidenceLevel}
            onChange={confidenceLevel => onChange({ confidenceLevel })}
          />
        </FilterField>
      </div>
    </div>
  )
}
