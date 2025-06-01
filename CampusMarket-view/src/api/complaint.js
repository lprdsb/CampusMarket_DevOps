import request from '@/utils/request';

export function submitComplaint(data) {
  return request({
    url: '/api/complaint/submit',
    method: 'post',
    data
  });
}

export function getMyComplaints(complainantId) {
  return request({
    url: '/api/complaint/my',
    method: 'get',
    params: { complainantId }
  });
}

export function getAllComplaints() {
  return request({
    url: '/api/complaint/all',
    method: 'get'
  });
}

export function handleComplaint(id, status) {
  return request({
    url: '/api/complaint/handle',
    method: 'post',
    params: { id, status }
  });
}