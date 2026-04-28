import { useMutation, useQueryClient } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

export function useCreateLesson(courseId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data) => apiClient.post(`/courses/${courseId}/lessons`, data).then(r => r.data.data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['lessons', courseId] }),
  })
}

export function useDeleteLesson(courseId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id) => apiClient.delete(`/lessons/${id}`),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['lessons', courseId] }),
  })
}

export function useReorderLessons(courseId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (itemIds) => apiClient.put(`/courses/${courseId}/lessons/reorder`, { itemIds }).then(r => r.data.data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['lessons', courseId] }),
  })
}
