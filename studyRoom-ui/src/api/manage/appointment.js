import request from '@/utils/request'

// 查询预约信息列表
export function listAppointment(query) {
  return request({
    url: '/manage/appointment/list',
    method: 'get',
    params: query
  })
}

// 查询预约信息详细
export function getAppointment(id) {
  return request({
    url: '/manage/appointment/' + id,
    method: 'get'
  })
}

// 新增预约信息
export function addAppointment(data) {
  return request({
    url: '/manage/appointment',
    method: 'post',
    data: data
  })
}

// 修改预约信息
export function updateAppointment(data) {
  return request({
    url: '/manage/appointment',
    method: 'put',
    data: data
  })
}

// 删除预约信息
export function delAppointment(id) {
  return request({
    url: '/manage/appointment/' + id,
    method: 'delete'
  })
}

// 导入预约信息
export function importAppointment(data) {
  return request({
    url: '/manage/appointment/importData',
    method: 'post',
    data: data
  })
}

// 下载预约信息导入模板
export function importTemplateAppointment() {
  return request({
    url: '/manage/appointment/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
