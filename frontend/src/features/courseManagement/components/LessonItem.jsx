export default function LessonItem({ item }) {
  if (item.type === 'VIDEO') {
    return (
      <div className="text-sm">
        <span className="font-medium">Video:</span> {item.title ?? item.videoUrl}
      </div>
    )
  }
  if (item.type === 'TEXT') {
    const preview = item.body?.slice(0, 60) ?? ''
    return (
      <div className="text-sm">
        <span className="font-medium">Text:</span> {preview}{item.body?.length > 60 ? '…' : ''}
      </div>
    )
  }
  if (item.type === 'QUIZ') {
    return (
      <div className="text-sm">
        <span className="font-medium">Quiz:</span> {item.questions?.length ?? 0} question{(item.questions?.length ?? 0) !== 1 ? 's' : ''}
      </div>
    )
  }
  if (item.type === 'SURVEY') {
    return (
      <div className="text-sm">
        <span className="font-medium">Survey:</span> {item.questions?.length ?? 0} question{(item.questions?.length ?? 0) !== 1 ? 's' : ''}
      </div>
    )
  }
  return <div className="text-sm">{item.type}</div>
}
