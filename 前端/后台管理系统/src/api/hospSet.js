import request from '@/utils/request'

//多条件分页查询
export default {
    // 获取医院设置列表
    getHospSetList(current, limit, searchObj) {
        return request({
            url: `/admin/hosp/hospitalSet/findPageHospSet/${current}/${limit}`,
            method: 'post',
            data: searchObj //使用json传递
        })
    },
    // 删除医院设置
    deleteHospSet(id) {
        return request({
            url: `/admin/hosp/hospitalSet/${id}`,
            method: 'delete'
        })
    },
    // 批量删除医院设置
    batchRemoveHospSet(idList) {
        return request({
            url: `/admin/hosp/hospitalSet/batchRemove`,
            method: 'delete',
            data: idList
        })
    },
    // 锁定和解锁
    lockHospSet(id, status) {
        return request({
            url: `/admin/hosp/hospitalSet/lockHospitalSet/${id}/${status}`,
            method: 'put'
        })
    },
    // 添加医院设置
    saveHospSet(hospSet) {
        return request({
            url: `/admin/hosp/hospitalSet/saveHospitalSet`,
            method: 'post',
            data: hospSet
        })
    },
    // 获取医院设置
    getHospSet(id) {
        return request({
            url: `/admin/hosp/hospitalSet/getHospSet/${id}`,
            method: 'get'
        })
    },
    // 更新医院设置
    updateHospSet(hospSet) {
        return request({
            url: `/admin/hosp/hospitalSet/updateHospitalSet`,
            method: 'post',
            data: hospSet
        })
    }

}