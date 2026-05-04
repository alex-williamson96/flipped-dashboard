import { keepPreviousData, useQuery } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

export function lessonTimelineQuery({ courseId, sectionId } = {}) {
  return {
    queryKey: ['analytics-timeline', courseId, sectionId],
    queryFn: () => {
      const params = { courseId }
      if (sectionId) params.sectionId = sectionId
      return apiClient.get('/analytics/lesson-timeline', { params }).then(r => r.data.data.lessons)
    },
  }
}

export default function useLessonTimeline({ courseId, sectionId } = {}) {
  return useQuery({
    ...lessonTimelineQuery({ courseId, sectionId }),
    enabled: !!courseId,
    placeholderData: keepPreviousData,
  })
}
