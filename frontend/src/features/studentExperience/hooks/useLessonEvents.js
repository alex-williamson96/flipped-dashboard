import { useMutation } from '@tanstack/react-query'
import apiClient from '../../../lib/apiClient'
import { useIdentity } from '../../roleSwitcher/IdentityContext'

export function useLessonStart() {
  const { studentId } = useIdentity()
  return useMutation({
    mutationFn: ({ lessonId }) =>
      apiClient.post('/activity/lesson-start', { studentId, lessonId }).then(r => r.data),
  })
}

export function useLessonComplete() {
  const { studentId } = useIdentity()
  return useMutation({
    mutationFn: ({ lessonId }) =>
      apiClient.post('/activity/lesson-complete', { studentId, lessonId }).then(r => r.data),
  })
}

export function useQuizResponse() {
  const { studentId } = useIdentity()
  return useMutation({
    mutationFn: ({ quizQuestionId, quizChoiceId }) =>
      apiClient.post('/activity/quiz-response', { studentId, quizQuestionId, quizChoiceId }).then(r => r.data),
  })
}

export function useSurveyResponse() {
  const { studentId } = useIdentity()
  return useMutation({
    mutationFn: ({ surveyQuestionId, likertValue, freeText }) =>
      apiClient.post('/activity/survey-response', { studentId, surveyQuestionId, likertValue, freeText }).then(r => r.data),
  })
}

export function useVideoWatched() {
  const { studentId } = useIdentity()
  return useMutation({
    mutationFn: ({ lessonItemId, watchPercent }) =>
      apiClient.post('/activity/video-watched', { studentId, lessonItemId, watchPercent }).then(r => r.data),
  })
}
