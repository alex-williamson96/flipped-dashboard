import { useQuery } from '@tanstack/react-query'
import apiClient from '../lib/apiClient'

export default function useCourse(courseId) {
  return useQuery({
    queryKey: ['course', courseId],
    queryFn: () => apiClient.get(`/courses/${courseId}`).then(r => r.data.data),
    enabled: !!courseId,
  })
}
