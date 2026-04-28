import { useState } from 'react'
import { useParams } from 'react-router-dom'
import useLessonItems from '../hooks/useLessonItems'
import LessonItemList from '../components/LessonItemList'
import AddVideoForm from '../components/AddVideoForm'
import AddTextForm from '../components/AddTextForm'
import QuizModal from '../components/QuizModal'
import SurveyModal from '../components/SurveyModal'

export default function LessonEditorPage() {
  const { lessonId } = useParams()
  const { data: lesson, isLoading, isError, error } = useLessonItems(lessonId)
  const [activeBlock, setActiveBlock] = useState(null)

  function hideBlock() {
    setActiveBlock(null)
  }

  return (
    <div className="p-6 max-w-3xl mx-auto">
      {isLoading && <span className="loading loading-spinner" />}
      {isError && <div className="alert alert-error">{error.message}</div>}
      {lesson && (
        <>
          <h1 className="text-2xl font-bold mb-4">{lesson.title}</h1>
          <LessonItemList lessonId={lessonId} items={lesson.items ?? []} />
          <div className="flex gap-2 mt-6 flex-wrap">
            <button
              className={`btn btn-sm ${activeBlock === 'video' ? 'btn-active' : 'btn-outline'}`}
              onClick={() => setActiveBlock(v => v === 'video' ? null : 'video')}
            >
              Add video
            </button>
            <button
              className={`btn btn-sm ${activeBlock === 'text' ? 'btn-active' : 'btn-outline'}`}
              onClick={() => setActiveBlock(v => v === 'text' ? null : 'text')}
            >
              Add text
            </button>
            <button
              className="btn btn-sm btn-outline"
              onClick={() => QuizModal.open()}
            >
              Add quiz
            </button>
            <button
              className="btn btn-sm btn-outline"
              onClick={() => SurveyModal.open()}
            >
              Add survey
            </button>
          </div>
          {activeBlock === 'video' && (
            <div className="mt-4">
              <AddVideoForm lessonId={lessonId} onSuccess={hideBlock} />
            </div>
          )}
          {activeBlock === 'text' && (
            <div className="mt-4">
              <AddTextForm lessonId={lessonId} onSuccess={hideBlock} />
            </div>
          )}
          <QuizModal lessonId={lessonId} />
          <SurveyModal lessonId={lessonId} />
        </>
      )}
    </div>
  )
}
