package com.khoja.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.khoja.classes.DynHost;
import com.khoja.classes.DynUser;
import com.khoja.classes.DB;
import com.khoja.classes.DynDomain;

public class AdminDao {

	public static ArrayList<DynHost> getHosts(DB dbinfo) {
		ArrayList<DynHost> hostlist = new ArrayList<DynHost>(); 
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn.prepareStatement("SELECT dyn_hosts.id, dyn_hosts.hostname, dyn_users.email, dyn_users.user, dyn_hosts.last_update, dyn_hosts.domain_id, dyn_domains.domain_name, dyn_hosts.ip_address FROM dyn_server_db.dyn_hosts, dyn_server_db.dyn_domains, dyn_server_db.dyn_users WHERE dyn_domains.id=dyn_hosts.domain_id AND dyn_hosts.dyn_user_id=dyn_users.id;");
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
            	int recid  = rs.getInt("id");
	            String hname = rs.getString("hostname");
	            String domname = rs.getString("domain_name");
	            String lupdate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(rs.getTimestamp("last_update"));
	            String ip = rs.getString("ip_address");
	            String uname = rs.getString("user");
	            String uemail = rs.getString("email");
	            hostlist.add(new DynHost(hname, domname, recid, lupdate, ip, uname, uemail));
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return hostlist;
	}
	
	public static ArrayList<DynUser> getUsers(DB dbinfo) {
		ArrayList<DynUser> userlist = new ArrayList<DynUser>(); 
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn.prepareStatement("SELECT dyn_users.id, dyn_users.email, dyn_users.user, dyn_users.enabled, dyn_users.create_date, dyn_users.admin FROM dyn_server_db.dyn_users;");
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
            	int userid  = rs.getInt("id");
	            String uname = rs.getString("user");
	            boolean admin = rs.getBoolean("admin");
	            boolean enabled = rs.getBoolean("enabled");
	            String email = rs.getString("email");
            	String ucreatedate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(rs.getTimestamp("create_date"));
	            userlist.add(new DynUser(email, userid, admin, uname, enabled, ucreatedate));
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return userlist;
	}
	
	public static ArrayList<DynDomain> getDomains(DB dbinfo) {
		
		
		ArrayList<DynDomain> domainlist = new ArrayList<DynDomain>(); 
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(dbinfo.getDriver()).newInstance();
            conn = DriverManager
                    .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn
                    .prepareStatement("SELECT id, domain_name, create_date FROM dyn_server_db.dyn_domains;");
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
            	int recid  = rs.getInt("id");
	            String domname = rs.getString("domain_name");
	            String createdate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(rs.getTimestamp("create_date"));
	            domainlist.add(new DynDomain(recid, domname, createdate));
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return domainlist;
	}
	
	
	public static boolean addDomain(String domainname, DB dbinfo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
            
		try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

			pst = conn
                .prepareStatement("insert into dyn_domains (domain_name) values (?);");
			pst.setString (1, domainname.trim());

			int success = pst.executeUpdate();
        
			if (success > 0) {   
				System.out.println("New Domain Added - Domain Name: "+domainname.trim());
				DNSDao.newDomain(domainname.trim(), dbinfo);
				result = true;
			} 
        
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public static boolean deleteUserHost(int hostid, DB dbinfo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
            
		try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

			pst = conn
                .prepareStatement("DELETE FROM dyn_hosts WHERE id = ?;");
			pst.setInt (1, hostid);

			int success = pst.executeUpdate();
        
			if (success > 0) {   
				System.out.println("Hostname Deleted - Hostname ID: "+hostid);
				
				result = true;
			} 
        
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
	
	public static boolean deleteUserHosts(int userid, DB dbinfo) {
			boolean result = false;
			Connection conn = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
	            
			try {
				Class.forName(dbinfo.getDriver()).newInstance();
				conn = DriverManager
	                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

				pst = conn
	                .prepareStatement("DELETE FROM dyn_hosts WHERE dyn_user_id = ?;");
				pst.setInt (1, userid);

				if (pst.executeUpdate() > -1) {   
					System.out.println("Domain Hostnames Deleted For User Successfull - User ID: "+userid);
					result = true;
					
				} else {
					System.out.println("Domain Hostnames Deleted For User Failed  - User ID: "+userid);
					result = false;
				}

	        
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (pst != null) {
					try {
						pst.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

			return result;
		}
	
	public static boolean deleteDomainHosts(int domid, DB dbinfo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
            
		try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

			pst = conn
                .prepareStatement("DELETE FROM dyn_hosts WHERE domain_id = ?;");
			pst.setInt (1, domid);
	        
			if (pst.executeUpdate() > -1) {   
				System.out.println("Domain Hostnames Deleted For Domain Successfull - Domain ID: "+domid);
				result = true;
				
			} else {
				System.out.println("Domain Hostnames Deleted For Domain Failed - Domain ID: "+domid);
				result = false;
			}
			
        
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
	
	public static boolean deleteDomain(int domainid, DB dbinfo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
            
		try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

			pst = conn
                .prepareStatement("DELETE FROM dyn_domains WHERE id = ?;");
			pst.setInt (1, domainid);

			int success = pst.executeUpdate();
        
			if (success > 0) {   
				System.out.println("Domain Deleted - Deleted Domain ID: "+domainid);
				result = true;
			} 
        
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
	
	public static boolean deleteUser(int userid, DB dbinfo) {
		boolean result = false;
	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
        
	try {
		Class.forName(dbinfo.getDriver()).newInstance();
		conn = DriverManager
            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

		pst = conn
            .prepareStatement("DELETE FROM dyn_users WHERE id = ?;");
		pst.setInt (1, userid);

		int success = pst.executeUpdate();
    
		if (success > 0) {   
			System.out.println("User Deleted - Deleted User ID: "+userid);
			result = true;
		} 
    
	} catch (Exception e) {
		System.out.println(e);
	} finally {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	return result;
}
	
	public static boolean enableAdmin(int userid, DB dbinfo) {
		boolean result = false;
    Connection conn = null;
    PreparedStatement pst = null;
    int recordUpdated = 0;
            
    try {
		Class.forName(dbinfo.getDriver()).newInstance();
		conn = DriverManager
            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

        pst = conn
                .prepareStatement("UPDATE dyn_server_db.dyn_users SET dyn_users.admin=1 WHERE dyn_users.id=?");
	        pst.setInt(1, userid);
        
        recordUpdated = pst.executeUpdate();
        
        if (recordUpdated > 0) {            
        	result = true;
        } 
        
    } catch (Exception e) {
        System.out.println(e);
    } finally {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
       
    }

    return result;
}


	
	public static boolean disableAdmin(int userid, DB dbinfo) {
		boolean result = false;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    int recordUpdated = 0;
	            
	    try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
	            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

	        pst = conn
	                .prepareStatement("UPDATE dyn_server_db.dyn_users SET dyn_users.admin=0 WHERE dyn_users.id=?");
	        pst.setInt(1, userid);
	        
	        recordUpdated = pst.executeUpdate();
	        
	        if (recordUpdated > 0) {            
	        	result = true;
	        } 
	        
	    } catch (Exception e) {
	        System.out.println(e);
	    } finally {
	        if (conn != null) {
	            try {
	                conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (pst != null) {
	            try {
	                pst.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	       
	    }

	    return result;
	}

	
	public static boolean enableUser(int userid, DB dbinfo) {
		boolean result = false;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    int recordUpdated = 0;
	            
	    try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
	            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

	        pst = conn
	                .prepareStatement("UPDATE dyn_server_db.dyn_users SET dyn_users.enabled=1 WHERE dyn_users.id=?");
	        pst.setInt(1, userid);
	        
	        recordUpdated = pst.executeUpdate();
	        
	        if (recordUpdated > 0) {            
	        	result = true;
	        } 
	        
	    } catch (Exception e) {
	        System.out.println(e);
	    } finally {
	        if (conn != null) {
	            try {
	                conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (pst != null) {
	            try {
	                pst.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	       
	    }

	    return result;
	}

	
	public static boolean disableUser(int userid, DB dbinfo) {
		boolean result = false;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    int recordUpdated = 0;
	            
	    try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
	            .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

	        pst = conn
	                .prepareStatement("UPDATE dyn_server_db.dyn_users SET dyn_users.enabled=0 WHERE dyn_users.id=?");
	        pst.setInt(1, userid);
	        recordUpdated = pst.executeUpdate();
	        
	        if (recordUpdated > 0) {            
	        	result = true;
	        } 
	        
	    } catch (Exception e) {
	        System.out.println(e);
	    } finally {
	        if (conn != null) {
	            try {
	                conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (pst != null) {
	            try {
	                pst.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	       
	    }

	    return result;
	}

	
	public static boolean doesDomainExist(String domain, DB dbinfo) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
                
        try {
			Class.forName(dbinfo.getDriver()).newInstance();
			conn = DriverManager
                .getConnection(dbinfo.getDBURL() + dbinfo.getDBName(), dbinfo.getUsername(), dbinfo.getPassword());

            pst = conn
                    .prepareStatement("SELECT * FROM dyn_server_db.dyn_domains WHERE domain_name=?;");
            
            pst.setString(1, domain);
            rs = pst.executeQuery();
            
            if (!rs.next()) {            
            	result = false;
            } 
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
	
}