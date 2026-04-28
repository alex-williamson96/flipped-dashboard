import { useDeleteLessonItem, useReorderLessonItems } from '../hooks/useLessonItemMutations'
import LessonItem from './LessonItem'

export default function LessonItemList({ lessonId, items = [] }) {
  const deleteItem = useDeleteLessonItem(lessonId)
  const reorderItems = useReorderLessonItems(lessonId)

  function moveUp(index) {
    const ids = items.map(i => i.id)
    const [item] = ids.splice(index, 1)
    ids.splice(index - 1, 0, item)
    reorderItems.mutate(ids)
  }

  function moveDown(index) {
    const ids = items.map(i => i.id)
    const [item] = ids.splice(index, 1)
    ids.splice(index + 1, 0, item)
    reorderItems.mutate(ids)
  }

  return (
    <div className="flex flex-col gap-2">
      {items.map((item, index) => (
        <div key={item.id} className="flex items-center gap-2 bg-base-200 rounded px-3 py-2">
          <div className="flex-1">
            <LessonItem item={item} />
          </div>
          <button
            className="btn btn-xs btn-ghost"
            disabled={index === 0 || reorderItems.isPending}
            onClick={() => moveUp(index)}
          >
            ↑
          </button>
          <button
            className="btn btn-xs btn-ghost"
            disabled={index === items.length - 1 || reorderItems.isPending}
            onClick={() => moveDown(index)}
          >
            ↓
          </button>
          <button
            className="btn btn-xs btn-ghost text-error"
            onClick={() => deleteItem.mutate(item.id)}
            disabled={deleteItem.isPending}
          >
            Delete
          </button>
        </div>
      ))}
      {reorderItems.isError && (
        <div className="alert alert-error py-2 text-sm">{reorderItems.error.message}</div>
      )}
      {deleteItem.isError && (
        <div className="alert alert-error py-2 text-sm">{deleteItem.error.message}</div>
      )}
    </div>
  )
}
