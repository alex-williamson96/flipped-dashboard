import { keepPreviousData, useQuery } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

export default function useLessonTimeline({ courseId, sectionId } = {}) {
  return useQuery({
    queryKey: ['analytics-timeline', courseId, sectionId],
    queryFn: () => {
      const params = { courseId }
      if (sectionId) params.sectionId = sectionId
      return apiClient.get('/analytics/lesson-timeline', { params }).then(r => r.data.data.lessons)
    },
    enabled: !!courseId,
    placeholderData: keepPreviousData,
  })
}
