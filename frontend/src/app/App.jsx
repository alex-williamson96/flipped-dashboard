import { QueryClientProvider } from '@tanstack/react-query'
import { RouterProvider } from 'react-router-dom'
import { IdentityProvider } from '../features/roleSwitcher'
import ProtectedRoute from '../auth/ProtectedRoute'
import queryClient from './queryClient'
import router from './router'

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ProtectedRoute>
        <IdentityProvider>
          <RouterProvider router={router} />
        </IdentityProvider>
      </ProtectedRoute>
    </QueryClientProvider>
  )
}
