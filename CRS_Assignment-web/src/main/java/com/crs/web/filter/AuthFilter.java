package com.crs.web.filter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;
@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/login.jsp", "/login",
            "/forgot_password.jsp", "/forgot_password",
            "/reset_password.jsp", "/reset_password"
    );
    @Override public void init(FilterConfig filterConfig) {}
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Use full URI then strip context path — works correctly with or without context root
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Allow static assets and public pages
        if (PUBLIC_PATHS.contains(path)
                || path.startsWith("/assets/")
                || path.startsWith("/css/")
                || path.startsWith("/js/")) {
            chain.doFilter(request, response);
            return;
        }

        // Check session
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String role = String.valueOf(session.getAttribute("role"));

        // Use full URI for RBAC so contains() works with any context root
        // e.g. /CRS_Assignment/academic/recovery_plan → contains("/academic/") = true
        String uri = req.getRequestURI();

        if (uri.contains("/admin/") && !"COURSE_ADMIN".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "COURSE_ADMIN role required.");
            return;
        }
        if (uri.contains("/academic/") && !"ACADEMIC_OFFICER".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "ACADEMIC_OFFICER role required.");
            return;
        }

        chain.doFilter(request, response);
    }
    @Override public void destroy() {}
}