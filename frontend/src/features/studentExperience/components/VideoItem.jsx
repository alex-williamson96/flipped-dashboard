import { useState } from 'react'
import { useVideoWatched } from '../hooks/useLessonEvents'

function extractVideoId(url) {
  if (!url) return null
  const watchMatch = url.match(/[?&]v=([^&]+)/)
  if (watchMatch) return watchMatch[1]
  const shortMatch = url.match(/youtu\.be\/([^?]+)/)
  if (shortMatch) return shortMatch[1]
  return null
}

export default function VideoItem({ item }) {
  const [watched, setWatched] = useState(false)
  const { mutate: markWatched } = useVideoWatched()
  const videoId = extractVideoId(item.videoUrl)

  const handleMarkWatched = () => {
    markWatched({ lessonItemId: item.lessonItemId, watchPercent: 100 })
    setWatched(true)
  }

  return (
    <div className="my-4">
      {videoId && (
        <iframe
          className="w-full aspect-video"
          src={`https://www.youtube.com/embed/${videoId}`}
          title="Lesson video"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowFullScreen
        />
      )}
      <button
        className="btn btn-sm mt-2"
        onClick={handleMarkWatched}
        disabled={watched}
      >
        Mark as watched
      </button>
    </div>
  )
}
