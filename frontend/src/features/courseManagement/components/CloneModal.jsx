import { useEffect, useRef, useState } from 'react'
import { useCloneCourse } from '../hooks/useCourseMutations'

export default function CloneModal({ courseId, onClose, onSuccess }) {
  const dialogRef = useRef(null)
  const [term, setTerm] = useState('')
  const cloneCourse = useCloneCourse()

  useEffect(() => {
    dialogRef.current?.showModal()
  }, [])

  async function handleConfirm() {
    if (!term.trim()) return
    try {
      const newCourse = await cloneCourse.mutateAsync({ courseId, term: term.trim() })
      onSuccess(newCourse)
    } catch (_) {
      // error displayed inline
    }
  }

  return (
    <dialog ref={dialogRef} className="modal" onClose={onClose}>
      <div className="modal-box">
        <h3 className="font-bold text-lg mb-3">Clone course</h3>
        <label className="label pb-1">
          <span className="label-text">New term</span>
        </label>
        <input
          className="input input-bordered input-sm w-full"
          placeholder="e.g. Spring 2026"
          value={term}
          onChange={e => setTerm(e.target.value)}
          required
        />
        {cloneCourse.isError && (
          <div className="alert alert-error py-2 text-sm mt-3">{cloneCourse.error?.response?.data?.error ?? cloneCourse.error?.message}</div>
        )}
        <div className="modal-action">
          <button className="btn btn-ghost" type="button" onClick={onClose} disabled={cloneCourse.isPending}>
            Cancel
          </button>
          <button
            className="btn btn-primary"
            type="button"
            onClick={handleConfirm}
            disabled={!term.trim() || cloneCourse.isPending}
          >
            {cloneCourse.isPending ? <span className="loading loading-spinner loading-sm" /> : 'Clone'}
          </button>
        </div>
      </div>
    </dialog>
  )
}
