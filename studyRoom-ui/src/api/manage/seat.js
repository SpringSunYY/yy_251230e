import request from '@/utils/request'

// 查询座位信息列表
export function listSeat(query) {
  return request({
    url: '/manage/seat/list',
    method: 'get',
    params: query
  })
}

// 查询座位信息详细
export function getSeat(id) {
  return request({
    url: '/manage/seat/' + id,
    method: 'get'
  })
}

// 新增座位信息
export function addSeat(data) {
  return request({
    url: '/manage/seat',
    method: 'post',
    data: data
  })
}

// 修改座位信息
export function updateSeat(data) {
  return request({
    url: '/manage/seat',
    method: 'put',
    data: data
  })
}

// 删除座位信息
export function delSeat(id) {
  return request({
    url: '/manage/seat/' + id,
    method: 'delete'
  })
}

// 导入座位信息
export function importSeat(data) {
  return request({
    url: '/manage/seat/importData',
    method: 'post',
    data: data
  })
}

// 下载座位信息导入模板
export function importTemplateSeat() {
  return request({
    url: '/manage/seat/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
