import { useQuery } from '@tanstack/react-query'
import apiClient from '../lib/apiClient'

export default function useSections(courseId) {
  return useQuery({
    queryKey: ['sections', courseId],
    queryFn: () => apiClient.get(`/courses/${courseId}/sections`).then(r => r.data.data),
    enabled: !!courseId,
  })
}
