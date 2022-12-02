let userURL = "http://localhost:8080/api/user";

formHeader();

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


async function formHeader() {
    const currentUser = await fetch(userURL).then(res => res.json());
    document.getElementById("headerEmail").innerHTML = currentUser.email;
    let roleString = " ";
    currentUser.roles.forEach(role => {
        roleString += role.roleName + " ";
    });
    document.getElementById("headerRoles").innerHTML = roleString;
}
