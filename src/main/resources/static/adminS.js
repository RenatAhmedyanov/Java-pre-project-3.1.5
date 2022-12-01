const url = "http://localhost:8080/api/index"

alert("mmm");
fetch(url).then(res => res.json()).then(fetchedUsers => document.getElementById("users-list").innerHTML = formUsersTable(fetchedUsers));

function formUsersTable(fetchedUsers) {
    let result = ' ';
    fetchedUsers.forEach(user => {
        result += `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td><td>`
        user.roles.forEach(role => {
            result += `
                    <span>${role.roleName} </span>`
        })
        result += `
                    </td><td><button class="button button1" data-id="${user.id}" data-bs-toggle="modal" data-bs-target="#editModal">Edit</button></td>
                    <td><button class="button button2" data-id="${user.id}" data-bs-toggle="modal" data-bs-target="#deleteModal">Delete</button></td>
                </tr>`
    })
    return result;
}