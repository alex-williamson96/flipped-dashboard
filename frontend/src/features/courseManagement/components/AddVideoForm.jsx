import { useState } from 'react'
import { useCreateLessonItem } from '../hooks/useLessonItemMutations'

function extractVideoId(url) {
  try {
    const u = new URL(url)
    if (u.hostname.includes('youtu.be')) return u.pathname.slice(1)
    return u.searchParams.get('v') ?? ''
  } catch {
    return ''
  }
}

export default function AddVideoForm({ lessonId, onSuccess }) {
  const [title, setTitle] = useState('')
  const [videoUrl, setVideoUrl] = useState('')
  const createItem = useCreateLessonItem(lessonId)
  const videoId = extractVideoId(videoUrl)

  function handleSubmit(e) {
    e.preventDefault()
    createItem.mutate({ type: 'VIDEO', videoUrl, title }, {
      onSuccess: () => {
        setTitle('')
        setVideoUrl('')
        onSuccess?.()
      },
    })
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-3 p-4 bg-base-200 rounded">
      <h4 className="font-semibold text-sm">Add video</h4>
      <input
        className="input input-bordered input-sm w-full"
        placeholder="YouTube URL"
        value={videoUrl}
        onChange={e => setVideoUrl(e.target.value)}
        required
      />
      {videoId && (
        <iframe
          className="w-full rounded"
          style={{ aspectRatio: '16/9' }}
          src={`https://www.youtube.com/embed/${videoId}`}
          allowFullScreen
          title="Preview"
        />
      )}
      <input
        className="input input-bordered input-sm w-full"
        placeholder="Title"
        value={title}
        onChange={e => setTitle(e.target.value)}
        required
      />
      {createItem.isError && (
        <div className="alert alert-error py-2 text-sm">{createItem.error.message}</div>
      )}
      <div className="flex gap-2">
        <button className="btn btn-sm btn-primary" type="submit" disabled={createItem.isPending}>
          {createItem.isPending ? <span className="loading loading-spinner loading-xs" /> : 'Add video'}
        </button>
        <button className="btn btn-sm btn-ghost" type="button" onClick={() => onSuccess?.()}>
          Cancel
        </button>
      </div>
    </form>
  )
}
