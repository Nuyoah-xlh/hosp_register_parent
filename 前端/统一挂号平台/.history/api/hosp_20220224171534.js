import request from '@/utils/request'

const api_name = `/api/hosp/hospital`

export default {
    // 查询医院列表
    getPageList(page, limit, searchObj) {
        return request({
            url: `${api_name}/findHospList/${page}/${limit}`,
            method: 'get',
            params: searchObj
        })
    },

    // 根据医院名称查询
    getByHosname(hosname) {
        return request({
            url: `${api_name}/findByHosname/${hosname}`,
            method: 'get'
        })
    },
    // 根据医院编号显示医院详情
    showDetail(hoscode) {
        return request({
            url: `${api_name}/findHospDetail/${hoscode}`,
            method: 'get'
        })
    },
    // 根据医院编号查科室
    getDepartments(hoscode) {
        return request({
            url: `${api_name}/department/${hoscode}`,
            method: 'get'
        })
    },
    // 获取可预约排班
    getBookingScheduleRule(page, limit, hoscode, depcode) {
        return request({
            url: `${api_name}/auth/getBookingScheduleRule/${page}/${limit}/${hoscode}/${depcode}`,
            method: 'get'
        })
    },
    findScheduleList(hoscode, depcode, workDate) {
        return request({
            url: `${api_name}/auth/findScheduleList/${hoscode}/${depcode}/${workDate}`,
            method: 'get'
        })
    },



}