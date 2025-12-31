import request from '@/utils/request'

// 查询通知列表
export function listNotification(query) {
  return request({
    url: '/manage/notification/list',
    method: 'get',
    params: query
  })
}

// 查询通知详细
export function getNotification(id) {
  return request({
    url: '/manage/notification/' + id,
    method: 'get'
  })
}

// 新增通知
export function addNotification(data) {
  return request({
    url: '/manage/notification',
    method: 'post',
    data: data
  })
}

// 修改通知
export function updateNotification(data) {
  return request({
    url: '/manage/notification',
    method: 'put',
    data: data
  })
}

// 删除通知
export function delNotification(id) {
  return request({
    url: '/manage/notification/' + id,
    method: 'delete'
  })
}

// 导入通知
export function importNotification(data) {
  return request({
    url: '/manage/notification/importData',
    method: 'post',
    data: data
  })
}

// 下载通知导入模板
export function importTemplateNotification() {
  return request({
    url: '/manage/notification/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
