const URL = "http://localhost:8080/api/index";

fetch(URL + "/roles").then(res => res.json()).then(roles => roles.forEach(role => {
        if (role.roleName.includes("USER")) {
            window.userRole = role;
        } else if (role.roleName.includes("ADMIN")) {
            window.adminRole = role;
        }
    }));

////FORMING ALL-USERS TAB
//сформируем таблицу и сохраним список пользователей в переменную для дальнейшего использования
let users = [];
fetch(URL).then(res => res.json()).then(fetchedUsers => document.getElementById("users-list").innerHTML = formUsersTable(fetchedUsers));
function formUsersTable(fetchedUsers) {
    users = fetchedUsers;
    let result = " ";
    users.forEach(user => {
        result += `<tr><td>${user.id}</td><td>${user.username}</td><td>${user.email}</td><td>`;
        user.roles.forEach(role => {
            result += `<span>${role.roleName} </span>`
        });
        result += `<td><button class="button button1" onclick="editButton(${user.id})" data-bs-toggle="modal" data-bs-target="#editModal">Edit</button></td>
                   <td><button class="button button2" onclick="deleteButton(${user.id})" data-bs-toggle="modal" data-bs-target="#deleteModal">Delete</button></td>`
    });
    return result;
}


////EDIT-FORM
// get-запросом получим и сохраним роли USER и ADMIN в отдельные переменные, для использования в форме редактирования
// let adminRole;
// let userRole;
// async function getAllRoles() {
//     let rolesURL = URL + "/roles";
//     let roles = await fetch(rolesURL).then(res => res.json());
//     roles.forEach(role => {
//         if (role.roleName.includes("USER")) {
//             window.userRole = role;
//         } else if (role.roleName.includes("ADMIN")) {
//             window.adminRole = role;
//         }
//     });
// }

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
            $("#selectUserE").attr("selected", "selected");
        } else if (role.roleName.includes("ADMIN")) {
            $("#selectAdminE").attr("selected", "selected");
        }
    });
}

//по ивенту submit получим данные из формы, добавим роли, сформируем объект user, сконвертируем его в JSON, отправим PUT запрос
let editForm = document.getElementById("editForm");
editForm.addEventListener("submit", (editUser) => {
    let editFormData = new FormData(editForm);
    let updatedUser = {roles: []};
    editForm.forEach(function(value, key) {
        updatedUser[key] = value;
    });
    $("#roleN").val().forEach(value => {
        if (value.includes("USER")) {
            alert("USER");
            updatedUser.roles.push(userRole);
        }
        if (value.includes("ADMIN")) {
            updatedUser.roles.push(adminRole);
        }
    });
    console.log(adminRole.roleName);
    fetch(URL, {method: "PUT", headers: {"Accept": "application/json", "Content-Type": "application/json; charset=UTF-8", "Referer": null}, body: JSON.stringify(newUser)})
        .then(data => updateUsers(data));
});

//обновим массив users и таблицу, что бы не перезагружать страницу после редактирования пользователя
function updateUsers(updatedUser) {
    let toBeUpdatedUserIndex = users.findIndex(x => x.id === updatedUser.id);
    users[toBeUpdatedUserIndex] = updatedUser;
    document.getElementById("users-list").innerHTML = formUsersTable(users);
}


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
            $("#selectUserD").attr("selected", "selected");
        } else if (role.roleName.includes("ADMIN")) {
            $("#selectAdminD").attr("selected", "selected");
        }
    });
}

//по ивенту submit отправим DELETE запрос
let deleteForm = document.getElementById("deleteForm")
deleteForm.addEventListener("submit", async (removeUser) => {
    await fetch(URL + "/" + deleteUserId, {method: 'DELETE'}).then(deletedUserId => deleteUser(deletedUserId));
})

//обновим массив users и таблицу после удаления пользователя
function deleteUser(id) {
    users = users.filter(x => x.id !== id);
    document.getElementById("users-list").innerHTML = formUsersTable(users);
}


////NEW USER FORM
let newUserForm = document.getElementById("newUserForm")
newUserForm.addEventListener("submit", (addNewUser) => {
    let newFormData = new FormData(newUserForm);
    let newUser = {roles: []};
    newFormData.forEach(function(value, key) {
        newUser[key] = value;
    });
    $("#roleN").val().forEach(value => {
        if (value.includes("USER")) {
            alert("USER");
            newUser.roles.push(userRole);
        }
        if (value.includes("ADMIN")) {
            alert("ADMIN")
            newUser.roles.push(adminRole);
        }
    })
    console.log(adminRole.roleName);
    console.log(newUser);
    fetch(URL, {method: "POST", headers: {"Accept": "application/json", "Content-Type": "application/json; charset=UTF-8", "Referer": null}, body: JSON.stringify(newUser)})
        .then(data => addUsers(data));
});

function addUsers(newUser) {
    users.push(newUser);
    document.getElementById("users-list").innerHTML = formUsersTable(users);
}
