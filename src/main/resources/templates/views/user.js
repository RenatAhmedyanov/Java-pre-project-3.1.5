let userURL = "http://localhost:8080/api/user";

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
