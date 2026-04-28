import { QueryClientProvider } from '@tanstack/react-query'
import { RouterProvider } from 'react-router-dom'
import { IdentityProvider, useIdentity } from '../features/roleSwitcher'
import WhoAreYou from '../features/roleSwitcher/WhoAreYou'
import ProtectedRoute from '../auth/ProtectedRoute'
import queryClient from './queryClient'
import router from './router'

function AppShell() {
  const { role } = useIdentity()
  if (!role) return <WhoAreYou />
  return <RouterProvider router={router} />
}

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ProtectedRoute>
        <IdentityProvider>
          <AppShell />
        </IdentityProvider>
      </ProtectedRoute>
    </QueryClientProvider>
  )
}
