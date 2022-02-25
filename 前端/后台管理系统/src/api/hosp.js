import request from '@/utils/request'

//多条件分页查询
export default {
    // 获取医院列表,参数包括页数、每页记录数、其他搜索条件
    getHospList(page, limit, searchObj) {
        return request({
            url: `/admin/hosp/hospital/list/${page}/${limit}`,
            method: 'get',
            params: searchObj
        })
    },
    // 根据dictCode查询所有子节点（所有省等信息）
    findByDictCode(dictCode) {
        return request({
            url: `/admin/cmn/dict/findByDictCode/${dictCode}`,
            method: 'get'
        })
    },
    // 查找子节点
    findChildrenById(id) {
        return request({
            url: `/admin/cmn/dict/findChildData/${id}`,
            method: 'get'
        })
    },
    // 更新状态
    updateStatus(id, status) {
        return request({
            url: `/admin/hosp/hospital/updateHospStatus/${id}/${status}`,
            method: 'get'
        })
    },
    // 查看医院详情
    getHospById(id) {
        return request({
            url: `/admin/hosp/hospital/showHospDetail/${id}`,
            method: 'get'
        })
    },
    // 查看医院科室
    getDeptByHoscode(hoscode) {
        return request({
            url: `/admin/hosp/department/getDeptList/${hoscode}`,
            method: 'get'
        })
    },
    // 查看排班
    getScheduleRule(page, limit, hoscode, depcode) {
        return request({
            url: `/admin/hosp/schedule/getScheduleRule/${page}/${limit}/${hoscode}/${depcode}`,
            method: 'get'
        })
    },
    // 查看排班详情
    getScheduleDetail(hoscode, depcode, workDate) {
        return request({
            url: `/admin/hosp/schedule/getScheduleDetail/${hoscode}/${depcode}/${workDate}`,
            method: 'get'
        })
    }

}