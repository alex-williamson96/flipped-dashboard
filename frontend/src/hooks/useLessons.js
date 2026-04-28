import { useQuery } from '@tanstack/react-query'
import apiClient from '../lib/apiClient'

export default function useLessons(courseId) {
  return useQuery({
    queryKey: ['lessons', courseId],
    queryFn: () => apiClient.get(`/courses/${courseId}/lessons`).then(r => r.data.data),
    enabled: !!courseId,
  })
}
