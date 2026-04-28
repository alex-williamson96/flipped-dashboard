export default function LogoutButton() {
  if (!import.meta.env.VITE_DEMO_PASSCODE) return null

  function handleClick() {
    localStorage.removeItem('demo_auth')
    localStorage.removeItem('identity.role')
    localStorage.removeItem('identity.studentId')
    localStorage.removeItem('identity.studentName')
    window.location.reload()
  }

  return (
    <button
      type="button"
      className="btn btn-ghost btn-xs"
      onClick={handleClick}
      aria-label="Lock the demo"
      title="Lock the demo"
    >
      Lock
    </button>
  )
}
