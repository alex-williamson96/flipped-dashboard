import { useMutation, useQueryClient } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

export function useCreateCourse() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data) => apiClient.post('/courses', data).then(r => r.data.data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['courses'] }),
  })
}

export function useArchiveCourse() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ courseId }) => apiClient.patch(`/courses/${courseId}`, { isArchived: true }).then(r => r.data.data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['courses'] }),
  })
}

export function useUnarchiveCourse() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ courseId }) => apiClient.patch(`/courses/${courseId}`, { isArchived: false }).then(r => r.data.data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['courses'] }),
  })
}

export function useCloneCourse() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ courseId, term }) => apiClient.post(`/courses/${courseId}/clone`, { term }).then(r => r.data.data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['courses'] }),
  })
}
