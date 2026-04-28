import { NavLink, Outlet } from 'react-router-dom'
import RoleSwitcher from '../features/roleSwitcher/RoleSwitcher'
import { useIdentity } from '../features/roleSwitcher/IdentityContext'
import ColdStartBanner from './ColdStartBanner'
import LogoutButton from './LogoutButton'
import ThemeToggle from './ThemeToggle'

export default function Layout() {
  const { role } = useIdentity()

  return (
    <>
      <ColdStartBanner />
      <nav className="flex items-center justify-between px-4 py-2 bg-base-100 border-b border-base-200">
        <div className="flex items-center gap-4">
          <span className="font-semibold">Flipped Dashboard</span>
          {role === 'teacher' && (
            <div className="join">
              <NavLink
                to="/teacher/dashboard"
                className={({ isActive }) => `btn btn-sm join-item ${isActive ? 'btn-primary' : 'btn-outline'}`}
              >
                Dashboard
              </NavLink>
              <NavLink
                to="/teacher/courses"
                className={({ isActive }) => `btn btn-sm join-item ${isActive ? 'btn-primary' : 'btn-outline'}`}
              >
                Courses
              </NavLink>
            </div>
          )}
        </div>
        <div className="flex items-center gap-2">
          <RoleSwitcher />
          <ThemeToggle />
          <LogoutButton />
        </div>
      </nav>
      <Outlet />
    </>
  )
}
