import { useQuery } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

export default function useLessonItems(lessonId) {
  return useQuery({
    queryKey: ['lesson', lessonId],
    queryFn: () => apiClient.get(`/lessons/${lessonId}`).then(r => r.data.data),
    enabled: !!lessonId,
  })
}
