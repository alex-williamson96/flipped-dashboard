import { useMutation, useQueryClient } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

export function useCreateSection(courseId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data) => apiClient.post(`/courses/${courseId}/sections`, data).then(r => r.data.data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['course', courseId] }),
  })
}

export function useUpdateSection(courseId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ id, ...data }) => apiClient.put(`/sections/${id}`, data).then(r => r.data.data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['course', courseId] }),
  })
}

export function useDeleteSection(courseId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id) => apiClient.delete(`/sections/${id}`),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['course', courseId] }),
  })
}
