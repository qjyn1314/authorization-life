import { getlanguage } from '@/api/common'

export function initlang () {
  getlanguage().then((res) => {
    return res.body.data
  })
}
