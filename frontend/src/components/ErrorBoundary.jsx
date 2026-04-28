import { Component } from 'react'

export default class ErrorBoundary extends Component {
  constructor(props) {
    super(props)
    this.state = { hasError: false, error: null }
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error }
  }

  componentDidCatch(error, info) {
    console.error('Uncaught error in React tree:', error, info)
  }

  handleReload = () => {
    window.location.reload()
  }

  render() {
    if (!this.state.hasError) return this.props.children

    return (
      <div className="min-h-screen flex items-center justify-center p-4">
        <div className="card card-bordered bg-base-100 p-6 max-w-md w-full">
          <h1 className="text-xl font-semibold mb-2">Something went wrong</h1>
          <p className="text-sm text-base-content/70 mb-4">
            The application hit an unexpected error. Reloading usually resolves it.
            If the problem persists, the backend may be starting up - please wait a moment and try again.
          </p>
          {this.state.error?.message && (
            <pre className="text-xs bg-base-200 p-2 rounded mb-4 overflow-x-auto">
              {this.state.error.message}
            </pre>
          )}
          <button className="btn btn-primary" onClick={this.handleReload}>
            Reload
          </button>
        </div>
      </div>
    )
  }
}
