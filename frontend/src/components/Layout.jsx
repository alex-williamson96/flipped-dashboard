import { NavLink, Outlet } from 'react-router-dom'
import RoleSwitcher from '../features/roleSwitcher/RoleSwitcher'
import { useIdentity } from '../features/roleSwitcher/IdentityContext'
import ColdStartBanner from './ColdStartBanner'
import LogoutButton from './LogoutButton'

export default function Layout() {
  const { role } = useIdentity()

  return (
    <>
      <ColdStartBanner />
      <nav className="flex items-center justify-between px-4 py-2 bg-base-100 border-b border-base-200">
        <div className="flex items-center gap-4">
          <span className="font-semibold">Flipped Dashboard</span>
          {role === 'teacher' && (
            <>
              <NavLink
                to="/teacher/courses"
                className={({ isActive }) => `text-sm ${isActive ? 'font-medium' : 'text-base-content/60 hover:text-base-content'}`}
              >
                Courses
              </NavLink>
              <NavLink
                to="/teacher/dashboard"
                className={({ isActive }) => `text-sm ${isActive ? 'font-medium' : 'text-base-content/60 hover:text-base-content'}`}
              >
                Dashboard
              </NavLink>
            </>
          )}
        </div>
        <div className="flex items-center gap-2">
          <RoleSwitcher />
          <LogoutButton />
        </div>
      </nav>
      <Outlet />
    </>
  )
}
