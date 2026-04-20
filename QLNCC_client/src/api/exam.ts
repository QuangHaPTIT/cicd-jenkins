import { getApiBaseUrl } from './config'

export type ExamSubmitPayload = {
  title: string
  note: string
}

const SUBMIT_PATH = '/api/v1/exam/submit'

/** POST ExamController — BE chỉ log, không lưu DB. */
export async function submitExam(payload: ExamSubmitPayload): Promise<void> {
  const res = await fetch(`${getApiBaseUrl()}${SUBMIT_PATH}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`)
  }
}
