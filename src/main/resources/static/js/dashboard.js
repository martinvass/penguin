function editActions() {
    const editButtons = document.querySelectorAll(".edit-btn");

    // Server inputs
    const serverIdEditInput = document.getElementById("serverEditId"); // hidden input
    const serverNameInput = document.getElementById("serverNameEdit");
    const serverAddressInput = document.getElementById("serverAddrEdit");
    const serverDescriptionInput = document.getElementById("serverDescEdit");

    // Group inputs
    const groupEditIdInput = document.getElementById("groupEditId"); // hidden input
    const groupNameInput = document.getElementById("groupNameEdit");
    const groupDescInput = document.getElementById("groupDescEdit");

    const serverGroupInput = document.getElementById("serverGroupEdit");
    const $options = Array.from(serverGroupInput.options);

    editButtons.forEach(button => {
        button.addEventListener("click", function () {
            // Server attributes
            const serverId = button.getAttribute("data-server-id");
            const serverName = button.getAttribute("data-server-name");
            const serverAddress = button.getAttribute("data-server-address");
            const serverDescription = button.getAttribute("data-server-description");

            // Group attributes
            const groupId = button.getAttribute("data-group-id");
            const groupName = button.getAttribute("data-group-name");
            const groupDesc = button.getAttribute("data-group-description");

            const serverGroup = button.getAttribute("data-server-group");
            const optionToSelect = $options.find(item => item.text === serverGroup);

            // Setting server fields
            serverIdEditInput.value = serverId;
            serverNameInput.value = serverName;
            serverAddressInput.value = serverAddress;
            serverDescriptionInput.value = serverDescription;
            if (optionToSelect === undefined) {
                $options[0].selected = true;
                serverGroupInput.value = 0;
            }
            else {
                optionToSelect.selected = true;
                serverGroupInput.value = optionToSelect.value;
            }

            // Setting group fields
            groupEditIdInput.value = groupId;
            groupNameInput.value = groupName;
            groupDescInput.value = groupDesc;
        });
    });
}

function deleteActions() {
    const delButtons = document.querySelectorAll(".del-btn");

    const serverIdInput = document.getElementById("serverId");
    const groupIdInput = document.getElementById("groupId");

    delButtons.forEach(button => {
        button.addEventListener("click", function () {
            serverIdInput.value = button.getAttribute("data-server-id");
            groupIdInput.value = button.getAttribute("data-group-id-deletion");
        });
    });
}

editActions();
deleteActions();