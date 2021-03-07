package com.example.networktest

//data class ThingsResponse( val data: ThingsResponse2, val description: String,val status: Int)

data class ThingsMe(
    val id: String,
    val username: String,
    val password: String,
    val email: String,
    val age: String,
    val enabled: String
)

data class searchNeedThings(var name:String,var queryAdmin :String, var queryPassword:String)

data class User(val username:String,val  password:String)


data class ThingsPost(val code: Int, val msg: String, val data: MyData)
data class MyData(val msg:String)


data class SearchProject(val code: Int, val msg: String, val data: AllProject)
data class AllProject(val data:List<Projects>)
data class Projects(
    val id: Int,
    val projectName: String,
    val sketchPorject: String,
    val mainProject: String,
    val manager: String,
    val member: String
)

data class ReleaseProject(val code: Int, val msg: String, val data: NewProject)
data class NewProject(val newProject: Projects)

data class ReleaseProjectMe(var account: String, var project:Project)

data class SearchRegister(var username:String,var password :String,var email:String)
data class ThingsResponse2(val timestamp :String, val status: Int, val  error:String, val message:String,val path:String)

data class ThingsResponse(val status:Int,val description:String,val data:ThingsResponseis)
data class ThingsResponseis(val id:String,val username:String,val password:String,val nicknames:String,
                            val email:String,val phone:String,val enabled:String,
                            val createtime:String,val remark:String)

//data class Things()

data class SearchDeptNotices(val code: Int, val msg: String, val data: SearchOneDeptNotices)
data class SearchOneDeptNotices(val notices: List<DeptNotices>)
data class DeptNotices(
    val id:String,
    val title: String,
    val items:String,
    val time: String,
    val deptName: String,
    val username: String,
    val account:String
)
data class ApproveDepartments(val code: Int, val msg: String, val data: ApproveOneDepartments)
class ApproveOneDepartments()

data class SearchPage(val total:Int,val list:List<SearchListPage>,
                      val pageNum:Int,val pageSize:Int,val size:Int,
                      val startRow:Int,val endRow:Int,val pages:Int,
                      val prePage:Int,val nextPage:Int,val isFirstPage:Boolean,
                      val isLastPage:Boolean,val hasPreviousPage:Boolean,val hasNextPage:Boolean,
                      val navigatePages:Int,val navigatepageNums:List<Int>,val navigateFirstPage:Int,
                      val navigateLastPage:Int
)
data class SearchListPage(val id:Int,val latitude:String,val longitude:String,val createTime:String)

//{
//    "total": 7,
//    "list": [
//    {
//        "id": 1,
//        "latitude": "21.5",
//        "longitude": "12",
//        "createTime": "2020-10-20 17:50:39"
//    },
//    {
//        "id": 2,
//        "latitude": "13",
//        "longitude": "55",
//        "createTime": "2020-10-20 17:53:28"
//    }
//    ],
//    "pageNum": 1,
//    "pageSize": 2,
//    "size": 2,
//    "startRow": 1,
//    "endRow": 2,
//    "pages": 4,
//    "prePage": 0,
//    "nextPage": 2,
//    "isFirstPage": true,
//    "isLastPage": false,
//    "hasPreviousPage": false,
//    "hasNextPage": true,
//    "navigatePages": 8,
//    "navigatepageNums": [
//    1,
//    2,
//    3,
//    4
//    ],
//    "navigateFirstPage": 1,
//    "navigateLastPage": 4
//}