import { keepPreviousData, useQuery } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

export const DEFAULT_FILTERS = {
  courseId: null,
  sectionId: null,
  lessonId: null,
  completionStatus: 'all',
  minQuizScore: null,
  maxQuizScore: null,
  confidenceLevel: null,
}

export function dashboardSummaryQuery(filters) {
  return {
    queryKey: ['analytics-overview', filters],
    queryFn: () => {
      const params = Object.fromEntries(
        Object.entries(filters).filter(([, v]) => v !== null && v !== undefined && v !== '')
      )
      return apiClient.get('/analytics/class-overview', { params }).then(r => r.data.data)
    },
  }
}

export default function useDashboardSummary(filters) {
  return useQuery({
    ...dashboardSummaryQuery(filters),
    enabled: !!filters?.courseId,
    placeholderData: keepPreviousData,
  })
}
