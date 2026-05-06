const API_BASE = "/api/drivers";

document.addEventListener("DOMContentLoaded", () => {
    bindAddForm();
    bindDriverGridActions();
});

function bindAddForm() {
    const addForm = document.getElementById("add-form");
    if (!addForm) return;

    addForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const name = document.getElementById("add-name")?.value;
        const dateOfBirth = document.getElementById("add-birthDate")?.value;
        const nationality = document.getElementById("add-nationality")?.value;
        const worldChampionships = document.getElementById("add-worldChampionships")?.value;
        const imageUrl = document.getElementById("add-imageUrl")?.value;

        const driver = await addDriver(name, dateOfBirth, nationality, worldChampionships, imageUrl);
        if (!driver || driver.name === undefined) return;

        addForm.reset();
        addDriverToDom(driver);
        updateDriversCount();
    });
}

function bindDriverGridActions() {
    const grid = document.getElementById("driversGrid");
    if (!grid) return;

    grid.addEventListener("click", async (event) => {
        const deleteButton = event.target.closest(".deleteDriverBtn");
        if (deleteButton) {
            const driverId = deleteButton.dataset.id;
            if (driverId) {
                await deleteDriver(driverId);
            }
            return;
        }

        const editButton = event.target.closest(".editDriverBtn");
        if (editButton) {
            await showEditForm(editButton);
        }
    });
}

async function getDriverById(id) {
    try {
        const res = await fetch(`${API_BASE}/${id}`, {
            method: "GET",
            headers: {
                Accept: "application/json"
            }
        });

        if (!res.ok) return null;
        return await res.json();
    } catch (error) {
        console.error(error);
        return null;
    }
}

async function addDriver(name, dateOfBirth, nationality, worldChampionships, imageUrl) {
    try {
        const res = await fetch(API_BASE, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
                ...buildCsrfHeader()
            },
            body: JSON.stringify({
                name,
                dateOfBirth,
                nationality,
                worldChampionships,
                imageUrl
            })
        });

        if (!res.ok) {
            if (res.status === 403) {
                alert("You need to be logged in to add a driver.");
            }
            return null;
        }

        return await res.json();
    } catch (error) {
        console.error(error);
        return null;
    }
}

async function deleteDriver(id) {
    if (!confirm("Are you sure you want to delete this driver?")) return;

    try {
        const res = await fetch(`${API_BASE}/${id}`, {
            method: "DELETE",
            headers: {
                ...buildCsrfHeader()
            }
        });

        if (res.ok) {
            alert("Driver deleted successfully!");
            const driverCard = document.querySelector(`[data-driver-id="${id}"]`);
            if (driverCard) {
                driverCard.remove();
                updateDriversCount();
            }
            return;
        }

        if (res.status === 404) {
            alert("Driver not found");
        } else if (res.status === 403) {
            alert("You need to be logged in to delete a driver.");
        } else {
            alert("Failed to delete driver");
        }
    } catch (error) {
        console.error(error);
    }
}

async function patchDriver(id, patchBody) {
    try {
        const res = await fetch(`${API_BASE}/${id}`, {
            method: "PATCH",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
                ...buildCsrfHeader()
            },
            body: JSON.stringify(patchBody)
        });

        if (res.ok) {
            return await res.json();
        }

        const errorText = await res.text();
        if (res.status === 400) {
            alert(`Invalid driver data (400). ${errorText || "Check name/nationality length, date, and championships (0-10)."}`);
        } else if (res.status === 403) {
            alert("You need to be logged in to edit a driver.");
        } else {
            alert("Failed to update driver.");
        }

        return null;
    } catch (error) {
        console.error(error);
        return null;
    }
}

function updateDriversCount() {
    const counter = document.getElementById("driversCount");
    if (!counter) return;

    counter.textContent = document.querySelectorAll("[data-driver-id]").length;
}

function addDriverToDom(driver) {
    const grid = document.getElementById("driversGrid");
    if (!grid) return;

    const col = document.createElement("div");
    col.className = "col";
    col.dataset.driverId = driver.id;
    col.innerHTML = createDriverCardHtml(driver);

    grid.prepend(col);
}

function createDriverCardHtml(driver) {
    return `
        <div class="card h-100 shadow-custom">
            <img alt="${driver.name || ""}" class="card-img-top" src="${driver.imageUrl || ""}"/>
            <div class="card-body">
                <h5 class="card-title fw-bold">${driver.name}</h5>
                <div class="mb-2">
                    <span class="badge bg-primary">${driver.nationality}</span>
                </div>
                <div class="mb-2 text-muted small">
                    <i class="bi bi-calendar me-1"></i>
                    <span>${driver.dateOfBirth}</span>
                </div>
                <div class="mb-2">
                    <i class="bi bi-trophy-fill text-warning me-1"></i>
                    <strong>${driver.worldChampionships}</strong>
                    <span class="text-muted small">Championships</span>
                </div>
            </div>
            <div class="card-footer d-flex gap-2">
                <a class="btn btn-sm btn-outline-primary flex-fill" href="/drivers/${driver.id}">
                    <i class="bi bi-eye me-1"></i>View
                </a>
                <button class="btn btn-sm btn-outline-danger deleteDriverBtn" data-id="${driver.id}">
                    <i class="bi bi-trash me-1"></i>Delete
                </button>
                <button class="btn btn-sm btn-outline-secondary editDriverBtn" data-id="${driver.id}">
                    Edit
                </button>
            </div>
        </div>
    `;
}

function renderDriverCard(driverId, driver) {
    const col = document.querySelector(`[data-driver-id="${driverId}"]`);
    if (!col) return;

    const title = col.querySelector(".card-title");
    if (title) title.textContent = driver.name || "";

    const badge = col.querySelector(".badge");
    if (badge) badge.textContent = driver.nationality || "";

    const dateElement = col.querySelector(".bi-calendar + span");
    if (dateElement) dateElement.textContent = driver.dateOfBirth || "";

    const championships = col.querySelector(".bi-trophy-fill + strong");
    if (championships) championships.textContent = String(driver.worldChampionships ?? 0);

    const image = col.querySelector("img.card-img-top");
    if (image) {
        image.src = driver.imageUrl || "";
        image.alt = driver.name || "";
    }

    const editContainer = col.querySelector(".driver-edit-container");
    if (editContainer) {
        editContainer.remove();
    }
}

function closeExistingEditForms() {
    const openForms = document.querySelectorAll(".driver-edit-container");
    openForms.forEach((form) => form.remove());
}

async function getTeams() {
    try {
        const res = await fetch(`/api/teams`, {
            method: "GET",
            headers: {
                Accept: "application/json"
            }
        });

        if (!res.ok) return null;

        const data = await res.json();

        console.log(data)
        return await data;

    } catch (error) {
        console.error(error);
        return null;
    }
}

async function showEditForm(button) {
    const driverId = button.dataset.id;
    if (!driverId) return;

    const col = document.querySelector(`[data-driver-id="${driverId}"]`);
    if (!col) return;

    const existingContainer = col.querySelector(".driver-edit-container");
    if (existingContainer) {
        existingContainer.remove();
        return;
    }

    const isAdmin = document.getElementById("driver-page")?.dataset.isAdmin === "true";

    closeExistingEditForms();

    const driver = await getDriverById(driverId);
    if (!driver) {
        alert("Could not load driver details for editing.");
        return;
    }

    const teams = await getTeams();

    const selectedTeamId = driver.simpleTeamDTO?.id ?? null;

    const teamOptions = (teams || []).map(team => `
      <option value="${team.id}" ${String(team.id) === String(selectedTeamId) ? "selected" : ""}>
          ${team.name}
      </option>
  `).join("");

    const cardBody = col.querySelector(".card-body");
    if (!cardBody) return;

    const formContainer = document.createElement("div");
    formContainer.className = "driver-edit-container mt-3 border-top pt-3";
    formContainer.innerHTML = `
        <div class="mb-2">
            <label class="form-label mb-1">Name</label>
            <input type="text" class="form-control form-control-sm edit-name" value="${driver.name || ""}">
        </div>
        <div class="mb-2">
            <label class="form-label mb-1">Nationality</label>
            <input type="text" class="form-control form-control-sm edit-nationality" value="${driver.nationality || ""}">
        </div>
        <div class="mb-2">
            <label class="form-label mb-1">Date of Birth</label>
            <input type="date" class="form-control form-control-sm edit-dateOfBirth" value="${driver.dateOfBirth || ""}">
        </div>
        <div class="mb-2">
            <label class="form-label mb-1">World Championships</label>
            <input type="number" min="0" max="10" class="form-control form-control-sm edit-worldChampionships" value="${driver.worldChampionships ?? 0}">
        </div>
        <div class="mb-2">
            <label class="form-label mb-1">Image URL</label>
            <input type="text" class="form-control form-control-sm edit-imageUrl" value="${driver.imageUrl || ""}">
        </div>
        ${isAdmin ? `
            <div class="mb-3">
            <label class="form-label mb-1">Team</label>
                <select class="form-select form-select-sm edit-teamId">
                    <option value="">No team</option>
                    ${teamOptions}
                </select>
            </div>` : ""
        }
        <div class="d-flex gap-2">
            <button type="button" class="btn btn-sm btn-primary edit-save-btn">Save</button>
            <button type="button" class="btn btn-sm btn-outline-secondary edit-cancel-btn">Cancel</button>
        </div>
    `;

    cardBody.appendChild(formContainer);

    const saveBtn = formContainer.querySelector(".edit-save-btn");
    const cancelBtn = formContainer.querySelector(".edit-cancel-btn");

    if (cancelBtn) {
        cancelBtn.addEventListener("click", () => {
            formContainer.remove();
        });
    }

    if (saveBtn) {
        saveBtn.addEventListener("click", async () => {
            const patchBody = buildPatchBody(formContainer);
            if (!patchBody) return;

            const updatedDriver = await patchDriver(driverId, patchBody);
            if (!updatedDriver) return;

            renderDriverCard(driverId, updatedDriver);
        });
    }
}

function buildPatchBody(formContainer) {
    const name = formContainer.querySelector(".edit-name")?.value?.trim() || "";
    const nationality = formContainer.querySelector(".edit-nationality")?.value?.trim() || "";
    const dateOfBirth = formContainer.querySelector(".edit-dateOfBirth")?.value || "";
    const championshipsRaw = formContainer.querySelector(".edit-worldChampionships")?.value?.trim() || "";
    const imageUrl = formContainer.querySelector(".edit-imageUrl")?.value?.trim() || "";
    const teamIdRaw = formContainer.querySelector(".edit-teamId")?.value || "";
    const teamId = teamIdRaw === "" ? null : Number(teamIdRaw);

    if (name?.length > 0 && name?.length < 2) {
        alert("Name must be at least 2 characters.");
        return null;
    }

    if (nationality?.length > 0 && nationality?.length < 2) {
        alert("Nationality must be at least 2 characters.");
        return null;
    }

    let championships = null;
    if (championshipsRaw !== "") {
        championships = Number(championshipsRaw);
        if (!Number.isInteger(championships) || championships < 0 || championships > 10) {
            alert("World championships must be a number between 0 and 10.");
            return null;
        }
    }

    return {
        name: name.length >= 2 ? name : null,
        nationality: nationality.length >= 2 ? nationality : null,
        dateOfBirth: dateOfBirth || null,
        worldChampionships: championships,
        imageUrl: imageUrl || null,
        teamId
    };
}

