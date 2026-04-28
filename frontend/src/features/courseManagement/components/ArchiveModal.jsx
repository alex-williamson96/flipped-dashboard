import { useEffect, useRef, useState } from 'react'
import { useArchiveCourse, useCloneCourse } from '../hooks/useCourseMutations'

export default function ArchiveModal({ course, onClose }) {
  const dialogRef = useRef(null)
  const [term, setTerm] = useState('')
  const archiveCourse = useArchiveCourse()
  const cloneCourse = useCloneCourse()

  useEffect(() => {
    dialogRef.current?.showModal()
  }, [])

  const isPending = archiveCourse.isPending || cloneCourse.isPending
  const error = archiveCourse.error || cloneCourse.error

  async function handleConfirm() {
    try {
      await archiveCourse.mutateAsync({ courseId: course.id })
      if (term.trim()) {
        await cloneCourse.mutateAsync({ courseId: course.id, term: term.trim() })
      }
      onClose()
    } catch (_) {
      // error displayed inline
    }
  }

  return (
    <dialog ref={dialogRef} className="modal" onClose={onClose}>
      <div className="modal-box">
        <h3 className="font-bold text-lg mb-3">Archive {course.title}?</h3>
        <p className="text-sm text-base-content/70 mb-4">
          This course will be archived and hidden from the active courses list.
        </p>
        <label className="label pb-1">
          <span className="label-text">Clone to new term (optional)</span>
        </label>
        <input
          className="input input-bordered input-sm w-full"
          placeholder="e.g. Spring 2026"
          value={term}
          onChange={e => setTerm(e.target.value)}
        />
        {error && <div className="alert alert-error py-2 text-sm mt-3">{error?.response?.data?.error ?? error?.message}</div>}
        <div className="modal-action">
          <button className="btn btn-ghost" type="button" onClick={onClose} disabled={isPending}>
            Cancel
          </button>
          <button className="btn btn-primary" type="button" onClick={handleConfirm} disabled={isPending}>
            {isPending ? <span className="loading loading-spinner loading-sm" /> : 'Archive'}
          </button>
        </div>
      </div>
    </dialog>
  )
}
