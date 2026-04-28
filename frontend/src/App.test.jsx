import { render } from '@testing-library/react'
import { describe, it } from 'vitest'
import App from './app/App'

describe('App', () => {
  it('renders without throwing', () => {
    render(<App />)
  })
})
