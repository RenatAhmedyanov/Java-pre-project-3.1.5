const URL = "http://localhost:8080/api/index";
const tabTrigger = new bootstrap.Tab(document.getElementById('users-table'))

//// get-запросом получим и сохраним роли USER и ADMIN в отдельные переменные, для использования в форме редактирования и создания нового пользователя
fetch(URL + "/roles").then(res => res.json()).then(roles => roles.forEach(role => {
    if (role.roleName.includes("USER")) {
        window.userRole = role;
    } else if (role.roleName.includes("ADMIN")) {
        window.adminRole = role;
    }
}));


////FORMING ALL-USERS TAB
//сформируем таблицу и сохраним список пользователей в переменную для дальнейшего использования
async function formUsersTable() {
    let users = await fetch(URL).then(res => res.json());
    let result = " ";
    users.forEach(user => {
        result += `<tr><td>${user.id}</td><td>${user.username}</td><td>${user.email}</td><td>`;
        user.roles.forEach(role => {
            result += `<span>${role.roleName} </span>`
        });
        result += `</td><td><button class="button button1" onclick="editButton(${user.id})" data-bs-toggle="modal" data-bs-target="#editModal">Edit</button></td>
                   <td><button class="button button2" onclick="deleteButton(${user.id})" data-bs-toggle="modal" data-bs-target="#deleteModal">Delete</button></td></tr>`
    });
    document.getElementById("users-list").innerHTML = result;
}

formUsersTable();

////EDIT-FORM
//по клику кнопки edit заполненим форму редактирования пользователя из существующих значений полей
async function editButton(userId) {
    let editURL = URL + "/" + userId;
    let user = await fetch(editURL).then(res => res.json());
    $("#idE").val(user.id);
    $("#usernameE").val(user.username);
    $("#emailE").val(user.email);
    let roles = user.roles;
    $("#selectUserE").attr("selected", false);
    $("#selectAdminE").attr("selected", false);
    roles.forEach(role => {
        if (role.roleName.includes("USER")) {
            $("#selectUserE").attr("selected", "selected")
        } else if (role.roleName.includes("ADMIN")) {
            $("#selectAdminE").attr("selected", "selected");
        }
    });
}

//по ивенту submit получим данные из формы, добавим роли, сформируем объект user, сконвертируем его в JSON, отправим PUT запрос
let editForm = document.getElementById("editForm");
editForm.addEventListener("submit", async (editUser) => {
    editUser.preventDefault()
    let editFormData = new FormData(editForm);
    let updatedUser = {roles: []};
    editFormData.forEach(function(value, key) {
        updatedUser[key] = value;
    });
    $("#roleE").val().forEach(value => {
        if (value.includes("USER")) {
            updatedUser.roles.push(userRole)
        }
        if (value.includes("ADMIN")) {
            updatedUser.roles.push(adminRole)
        }
    })
    const data = await fetch(URL, {method: "PUT", headers: {"Accept": "application/json", "Content-Type": "application/json; charset=UTF-8", "Referer": null}, body: JSON.stringify(updatedUser)}).catch((e) => console.error(e))
    formUsersTable();
    tabTrigger.show()
    $("#editModal").modal("hide");
});


////DELETE FORM
//по клику кнопки delete заполненим форму удаления пользователя, сохраним ID пользователя для передачи в DELETE метод
let deleteUserId;
async function deleteButton(userId) {
    deleteUserId = userId;
    let deleteURL = URL + "/" + userId;
    let user = await fetch(deleteURL).then(res => res.json());
    $('#idD').val(user.id);
    $('#usernameD').val(user.username);
    $('#emailD').val(user.email);
    $("#selectUserD").attr("selected", false);
    $("#selectAdminD").attr("selected", false);
    let roles = user.roles;
    roles.forEach(role => {
        if (role.roleName.includes("USER")) {
            $("#selectUserD").attr("selected", "selected")
        } else if (role.roleName.includes("ADMIN")) {
            $("#selectAdminD").attr("selected", "selected");
        }
    });
}

//по ивенту submit отправим DELETE запрос
let deleteForm = document.getElementById("deleteForm")
deleteForm.addEventListener("submit", async (removeUser) => {
    await fetch(URL + "/" + deleteUserId, {method: 'DELETE'}).then(deletedUserId => deleteUser(deletedUserId));
    formUsersTable();
})

////NEW USER FORM
//по ивенту submit пользователя для передачи в POST метод
const newUserForm = document.getElementById("newUserForm")
newUserForm.addEventListener("submit",async (event) => {
    event.preventDefault();
    let newFormData = new FormData(newUserForm);
    let newUser = {roles: []};
    newFormData.forEach(function(value, key) {
        newUser[key] = value;
    });
    $("#roleN").val().forEach(value => {
        if (value.includes("USER")) {
            newUser.roles.push(window.userRole)
        }
        if (value.includes("ADMIN")) {
            newUser.roles.push(window.adminRole)
        }
    })
    const data = await fetch(URL, {method: "POST", headers: {"Accept": "application/json", "Content-Type": "application/json; charset=UTF-8", "Referer": null}, body: JSON.stringify(newUser)})
        .then(() => newUserForm.reset())
        .catch((e) => console.error(e))
    formUsersTable();
    tabTrigger.show()
});


//// FORM USER-INFO TAB FOR ADMIN PANEL
let userURL = "http://localhost:8080/api/user";
document.getElementById("user-panel").addEventListener("click", (show => {
    fetch(userURL).then(res => res.json()).then(data => document.getElementById("userInfo").innerHTML = formUserInfoTable(data));
    function formUserInfoTable(user) {
        let result = " ";
        result = `<tr><td>${user.id}</td><td>${user.username}</td><td>${user.email}</td><td>`;
        user.roles.forEach(role => {
            result += `<span>${role.roleName} </span>`
        });
        result += `</td></tr>`;
        return result;
    }
}))

