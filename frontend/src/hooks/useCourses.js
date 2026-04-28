import { useQuery } from '@tanstack/react-query'
import apiClient from '../lib/apiClient'

export default function useCourses(archived) {
  const url = archived != null ? `/courses?archived=${archived}` : '/courses'
  return useQuery({
    queryKey: ['courses', archived ?? 'active'],
    queryFn: () => apiClient.get(url).then(r => r.data.data),
  })
}
