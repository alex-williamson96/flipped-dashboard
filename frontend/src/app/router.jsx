import { createBrowserRouter } from 'react-router-dom'
import Layout from '../components/Layout'
import CourseListPage from '../features/courseManagement/pages/CourseListPage'
import CourseDetailPage from '../features/courseManagement/pages/CourseDetailPage'
import LessonEditorPage from '../features/courseManagement/pages/LessonEditorPage'
import StudentHomePage from '../features/studentExperience/pages/StudentHomePage'
import LessonListPage from '../features/studentExperience/pages/LessonListPage'
import LessonViewPage from '../features/studentExperience/pages/LessonViewPage'
import LessonCompletionPage from '../features/studentExperience/pages/LessonCompletionPage'
import DashboardPage from '../features/analyticsDashboard/pages/DashboardPage'

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      { path: 'teacher/courses', element: <CourseListPage /> },
      { path: 'teacher/courses/:courseId', element: <CourseDetailPage /> },
      { path: 'teacher/courses/:courseId/lessons/:lessonId', element: <LessonEditorPage /> },
      { path: 'teacher/dashboard', element: <DashboardPage /> },
      { path: 'student', element: <StudentHomePage /> },
      { path: 'student/courses/:courseId/lessons', element: <LessonListPage /> },
      { path: 'student/lessons/:lessonId', element: <LessonViewPage /> },
      { path: 'student/lessons/:lessonId/completion', element: <LessonCompletionPage /> },
    ],
  },
])

export default router
