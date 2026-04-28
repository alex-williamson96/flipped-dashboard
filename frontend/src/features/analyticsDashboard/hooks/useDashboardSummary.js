import { keepPreviousData, useQuery } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

export default function useDashboardSummary(filters) {
  return useQuery({
    queryKey: ['analytics-overview', filters],
    queryFn: () => {
      const params = Object.fromEntries(
        Object.entries(filters).filter(([, v]) => v !== null && v !== undefined && v !== '')
      )
      return apiClient.get('/analytics/class-overview', { params }).then(r => r.data.data)
    },
    enabled: !!filters?.courseId,
    placeholderData: keepPreviousData,
  })
}
