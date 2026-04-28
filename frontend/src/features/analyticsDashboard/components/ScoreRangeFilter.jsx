import { useState, useEffect, useRef } from 'react'

export default function ScoreRangeFilter({ maxScore, onChange }) {
  const [local, setLocal] = useState(maxScore ?? '')
  const timerRef = useRef(null)

  useEffect(() => {
    setLocal(maxScore ?? '')
  }, [maxScore])

  function handleChange(rawValue) {
    setLocal(rawValue)
    clearTimeout(timerRef.current)
    timerRef.current = setTimeout(() => {
      onChange(rawValue === '' ? null : Number(rawValue))
    }, 400)
  }

  return (
    <input
      type="number"
      className="input input-sm w-24 pl-0"
      min={0}
      max={100}
      placeholder="70"
      value={local}
      onChange={e => handleChange(e.target.value)}
    />
  )
}
