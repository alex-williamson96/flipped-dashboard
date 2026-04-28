import { useMutation, useQueryClient } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'

export function useCreateLessonItem(lessonId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data) => apiClient.post(`/lessons/${lessonId}/items`, data).then(r => r.data.data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['lesson', lessonId] }),
  })
}

export function useDeleteLessonItem(lessonId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (id) => apiClient.delete(`/lesson-items/${id}`),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['lesson', lessonId] }),
  })
}

export function useReorderLessonItems(lessonId) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (itemIds) => apiClient.put(`/lessons/${lessonId}/items/reorder`, { itemIds }).then(r => r.data.data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['lesson', lessonId] }),
  })
}
