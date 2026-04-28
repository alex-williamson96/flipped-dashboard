import { keepPreviousData, useQuery } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

export default function useStudentDetail({ studentId, courseId, lessonId } = {}) {
  return useQuery({
    queryKey: ['analytics-student', studentId, courseId, lessonId],
    queryFn: () => {
      const params = { courseId }
      if (lessonId) params.lessonId = lessonId
      return apiClient.get(`/analytics/student/${studentId}`, { params }).then(r => r.data.data)
    },
    enabled: !!studentId && !!courseId,
    placeholderData: keepPreviousData,
  })
}
