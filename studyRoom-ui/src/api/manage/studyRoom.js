import request from '@/utils/request'

// 查询自习室信息列表
export function listStudyRoom(query) {
  return request({
    url: '/manage/studyRoom/list',
    method: 'get',
    params: query
  })
}

// 查询自习室信息详细
export function getStudyRoom(id) {
  return request({
    url: '/manage/studyRoom/' + id,
    method: 'get'
  })
}

// 新增自习室信息
export function addStudyRoom(data) {
  return request({
    url: '/manage/studyRoom',
    method: 'post',
    data: data
  })
}

// 修改自习室信息
export function updateStudyRoom(data) {
  return request({
    url: '/manage/studyRoom',
    method: 'put',
    data: data
  })
}

// 删除自习室信息
export function delStudyRoom(id) {
  return request({
    url: '/manage/studyRoom/' + id,
    method: 'delete'
  })
}

// 导入自习室信息
export function importStudyRoom(data) {
  return request({
    url: '/manage/studyRoom/importData',
    method: 'post',
    data: data
  })
}

// 下载自习室信息导入模板
export function importTemplateStudyRoom() {
  return request({
    url: '/manage/studyRoom/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
