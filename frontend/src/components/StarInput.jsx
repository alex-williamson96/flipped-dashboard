import { useState } from 'react'

export default function StarInput({ value, onChange, disabled = false }) {
  const [hovered, setHovered] = useState(0)

  const filled = hovered > 0 ? hovered : value

  return (
    <div className="flex gap-1" onMouseLeave={() => disabled || setHovered(0)}>
      {[1, 2, 3, 4, 5].map(n => (
        <button
          key={n}
          type="button"
          disabled={disabled}
          aria-label={n <= filled ? `star ${n} filled` : `star ${n} empty`}
          onMouseEnter={() => disabled || setHovered(n)}
          onClick={() => onChange(n)}
        >
          <svg width="24" height="24" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            {n <= filled ? (
              <polygon
                points="12,2 15.09,8.26 22,9.27 17,14.14 18.18,21.02 12,17.77 5.82,21.02 7,14.14 2,9.27 8.91,8.26"
                fill="currentColor"
                stroke="currentColor"
                strokeWidth="1"
              />
            ) : (
              <polygon
                points="12,2 15.09,8.26 22,9.27 17,14.14 18.18,21.02 12,17.77 5.82,21.02 7,14.14 2,9.27 8.91,8.26"
                fill="none"
                stroke="currentColor"
                strokeWidth="1"
              />
            )}
          </svg>
        </button>
      ))}
    </div>
  )
}
