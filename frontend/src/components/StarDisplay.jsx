export default function StarDisplay({ value }) {
  return (
    <div className="flex gap-1">
      {[1, 2, 3, 4, 5].map(n => {
        const isFullyFilled = value >= n
        const isPartial = !isFullyFilled && value > n - 1 && value < n
        const gradientId = `star-grad-${n}`
        const fillPct = isPartial ? ((value - (n - 1)) * 100).toFixed(1) : 0

        return (
          <svg key={n} role="img" width="24" height="24" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-label={isFullyFilled ? `star ${n} filled` : isPartial ? `star ${n} partial` : `star ${n} empty`}>
            {isPartial && (
              <defs>
                <linearGradient id={gradientId} x1="0" x2="1" y1="0" y2="0">
                  <stop offset={`${fillPct}%`} stopColor="currentColor" />
                  <stop offset={`${fillPct}%`} stopColor="transparent" />
                </linearGradient>
              </defs>
            )}
            <polygon
              points="12,2 15.09,8.26 22,9.27 17,14.14 18.18,21.02 12,17.77 5.82,21.02 7,14.14 2,9.27 8.91,8.26"
              fill={isFullyFilled ? 'currentColor' : isPartial ? `url(#${gradientId})` : 'none'}
              stroke="currentColor"
              strokeWidth="1"
            />
          </svg>
        )
      })}
    </div>
  )
}
