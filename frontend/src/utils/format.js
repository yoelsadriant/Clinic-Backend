export const formatRupiah = (amount) => {
  return 'Rp. ' + Number(amount).toLocaleString('id-ID')
}

export const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('id-ID')
}

export const formatTime = (time) => {
  if (!time) return '-'
  return time.substring(0, 5) // HH:MM
}

export const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  const d = new Date(datetime)
  return d.toLocaleDateString('id-ID') + ' ' + d.toLocaleTimeString('id-ID', { hour: '2-digit', minute: '2-digit' })
}
