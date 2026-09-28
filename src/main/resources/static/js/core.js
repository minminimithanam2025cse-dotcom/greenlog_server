/* =========================================================
   GREENLOG - CORE JAVASCRIPT SYSTEM
   Reusable API Helper, Auth Guard, Modals, Toasts & Formatting
   ========================================================= */

"use strict";

const API_BASE = "/api";

/* Demo authentication credentials (frontend only) */
const DEMO_USERS = {
    admin: {
        username: "admin",
        password: "admin123",
        role: "admin",
        name: "GreenLog Administrator"
    },
    user: {
        username: "user",
        password: "user123",
        role: "user",
        name: "GreenLog User"
    }
};

/* =========================================================
   AUTHENTICATION MANAGER
   ========================================================= */
const A = {
    role() {
        return localStorage.getItem("greenlog_role");
    },
    user() {
        return localStorage.getItem("greenlog_user");
    },
    name() {
        return localStorage.getItem("greenlog_name");
    },
    isLoggedIn() {
        return !!this.role();
    },
    login(username, password, role) {
        const expectedRole = (role || "").toLowerCase();
        const account = DEMO_USERS[expectedRole];

        if (!account) {
            return { success: false, message: "Invalid login type." };
        }

        if (
            (username || "").trim() !== account.username ||
            password !== account.password
        ) {
            return { success: false, message: "Invalid username or password." };
        }

        localStorage.setItem("greenlog_user", account.username);
        localStorage.setItem("greenlog_role", account.role);
        localStorage.setItem("greenlog_name", account.name);

        return {
            success: true,
            role: account.role,
            username: account.username,
            name: account.name
        };
    },
    logout() {
        localStorage.removeItem("greenlog_user");
        localStorage.removeItem("greenlog_role");
        localStorage.removeItem("greenlog_name");
        window.location.href = "../index.html";
    },
    guard(requiredRole) {
        const currentRole = this.role();
        if (!currentRole) {
            window.location.href = "../index.html";
            return false;
        }

        if (
            requiredRole &&
            currentRole.toLowerCase() !== requiredRole.toLowerCase()
        ) {
            if (currentRole === "admin") {
                window.location.href = "../admin/dashboard.html";
            } else {
                window.location.href = "../user/dashboard.html";
            }
            return false;
        }

        return true;
    }
};

/* =========================================================
   REUSABLE API HELPER
   ========================================================= */
async function api(path, options = {}) {
    let cleanPath = path || "";

    // Normalize path so "/api/volunteers" and "/volunteers" both work correctly
    if (cleanPath.startsWith("/api/")) {
        cleanPath = cleanPath.substring(4);
    } else if (cleanPath === "/api") {
        cleanPath = "";
    }

    if (!cleanPath.startsWith("/")) {
        cleanPath = "/" + cleanPath;
    }

    const fullUrl = API_BASE + cleanPath;

    const requestOptions = {
        ...options,
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            ...(options.headers || {})
        }
    };

    try {
        const response = await fetch(fullUrl, requestOptions);

        if (response.status === 204) {
            return null;
        }

        const contentType = response.headers.get("content-type") || "";
        let data;

        if (contentType.includes("application/json")) {
            data = await response.json();
        } else {
            data = await response.text();
        }

        if (!response.ok) {
            let message = `HTTP Error ${response.status}`;
            if (data && typeof data === "object") {
                message = data.message || data.error || data.detail || message;
                if (data.messages && typeof data.messages === "object") {
                    const vals = Object.values(data.messages);
                    if (vals.length > 0) message = vals.join(", ");
                }
            } else if (typeof data === "string" && data.trim()) {
                message = data;
            }
            const err = new Error(message);
            err.status = response.status;
            throw err;
        }

        return data;
    } catch (error) {
        console.error("API error:", error);
        if (error instanceof TypeError && error.message && error.message.includes("fetch")) {
            throw new Error("Unable to connect to GreenLog server.");
        }
        throw error;
    }
}

async function get(path) {
    return api(path, { method: "GET" });
}

async function post(path, data) {
    return api(path, {
        method: "POST",
        body: JSON.stringify(data)
    });
}

async function put(path, data) {
    return api(path, {
        method: "PUT",
        body: JSON.stringify(data)
    });
}

async function del(path) {
    return api(path, { method: "DELETE" });
}

/* =========================================================
   UTILITY & FORMATTING HELPERS
   ========================================================= */
function esc(value) {
    if (value === null || value === undefined) return "";
    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

function date(value) {
    if (!value) return "-";
    try {
        const d = new Date(value);
        if (Number.isNaN(d.getTime())) return esc(value);
        return d.toLocaleDateString("en-IN", {
            day: "2-digit",
            month: "short",
            year: "numeric"
        });
    } catch (e) {
        return esc(value);
    }
}

function dateInput(value) {
    if (!value) return "";
    try {
        const d = new Date(value);
        if (Number.isNaN(d.getTime())) return "";
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, "0");
        const day = String(d.getDate()).padStart(2, "0");
        return `${year}-${month}-${day}`;
    } catch (e) {
        return "";
    }
}

function number(value) {
    const num = Number(value);
    if (Number.isNaN(num)) return "0";
    return num.toLocaleString("en-IN");
}

function percentage(value) {
    const num = Number(value);
    if (Number.isNaN(num)) return "0%";
    return `${num.toFixed(1)}%`;
}

function statusBadge(status) {
    if (!status) return '<span class="badge badge-neutral">-</span>';
    const s = String(status).trim().toUpperCase();
    let cls = "badge-neutral";
    if (s === "ALIVE" || s === "ACTIVE") cls = "badge-success";
    else if (s === "DEAD" || s === "INACTIVE") cls = "badge-danger";
    else if (s === "PENDING" || s === "WARNING") cls = "badge-warning";
    return `<span class="badge ${cls}">${esc(s)}</span>`;
}

function initials(name) {
    if (!name) return "?";
    const parts = String(name).trim().split(/\s+/);
    if (parts.length === 1) return parts[0].substring(0, 2).toUpperCase();
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
}

/* =========================================================
   TOAST NOTIFICATION SYSTEM
   ========================================================= */
function toast(message, type = "success") {
    let container = document.querySelector(".toast-container");
    if (!container) {
        container = document.createElement("div");
        container.className = "toast-container";
        document.body.appendChild(container);
    }

    const item = document.createElement("div");
    item.className = `toast ${type}`;
    item.textContent = message;
    container.appendChild(item);

    setTimeout(() => {
        item.style.opacity = "0";
        item.style.transform = "translateX(15px)";
        setTimeout(() => item.remove(), 250);
    }, 3200);
}

/* =========================================================
   MODAL DIALOG CONTROLS
   ========================================================= */
function openModal(id) {
    const modal = document.getElementById(id);
    if (!modal) return;
    modal.classList.add("show");
    document.body.style.overflow = "hidden";
}

function closeModal(id) {
    if (!id) {
        document.querySelectorAll(".modal.show").forEach(m => {
            m.classList.remove("show");
        });
    } else {
        const modal = document.getElementById(id);
        if (modal) modal.classList.remove("show");
    }
    document.body.style.overflow = "";
}

/* Click overlay or ESC to close modals */
document.addEventListener("click", function (event) {
    if (event.target && event.target.classList && event.target.classList.contains("modal")) {
        closeModal();
    }
});

document.addEventListener("keydown", function (event) {
    if (event.key === "Escape") {
        closeModal();
    }
});

/* =========================================================
   COMMON UI INITIALIZATION
   ========================================================= */
function applyUserInfo() {
    const uName = A.name() || A.user() || "User";
    const uRole = A.role() || "user";
    const roleFormatted = uRole.charAt(0).toUpperCase() + uRole.slice(1);

    document.querySelectorAll("[data-user-name]").forEach(el => {
        el.textContent = uName;
    });

    document.querySelectorAll("[data-user-role]").forEach(el => {
        el.textContent = roleFormatted;
    });
}

function showError(error) {
    console.error("GreenLog Error:", error);
    const msg = error?.message || "Something went wrong.";
    toast(msg, "error");
}

document.addEventListener("DOMContentLoaded", function () {
    applyUserInfo();

    document.querySelectorAll("[data-logout]").forEach(btn => {
        btn.addEventListener("click", function (e) {
            e.preventDefault();
            A.logout();
        });
    });

    document.querySelectorAll("[data-modal-close]").forEach(btn => {
        btn.addEventListener("click", function () {
            const targetId = btn.getAttribute("data-modal-close");
            closeModal(targetId);
        });
    });
});

window.GreenLog = {
    API_BASE,
    A,
    api,
    get,
    post,
    put,
    del,
    esc,
    date,
    dateInput,
    number,
    percentage,
    statusBadge,
    initials,
    toast,
    openModal,
    closeModal,
    showError,
    applyUserInfo
};