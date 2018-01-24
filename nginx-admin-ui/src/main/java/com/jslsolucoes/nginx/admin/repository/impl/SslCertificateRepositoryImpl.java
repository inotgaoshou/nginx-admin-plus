package com.jslsolucoes.nginx.admin.repository.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import com.jslsolucoes.nginx.admin.model.Nginx;
import com.jslsolucoes.nginx.admin.model.Nginx_;
import com.jslsolucoes.nginx.admin.model.Server_;
import com.jslsolucoes.nginx.admin.model.SslCertificate;
import com.jslsolucoes.nginx.admin.model.SslCertificate_;
import com.jslsolucoes.nginx.admin.repository.NginxRepository;
import com.jslsolucoes.nginx.admin.repository.ResourceIdentifierRepository;
import com.jslsolucoes.nginx.admin.repository.SslCertificateRepository;

@RequestScoped
public class SslCertificateRepositoryImpl extends RepositoryImpl<SslCertificate> implements SslCertificateRepository {

	private NginxRepository nginxRepository;
	private ResourceIdentifierRepository resourceIdentifierRepository;

	public SslCertificateRepositoryImpl() {
		// Default constructor
	}

	@Inject
	public SslCertificateRepositoryImpl(EntityManager entityManager, NginxRepository nginxRepository,
			ResourceIdentifierRepository resourceIdentifierRepository) {
		super(entityManager);
		this.nginxRepository = nginxRepository;
		this.resourceIdentifierRepository = resourceIdentifierRepository;
	}

	@Override
	public OperationType deleteWithResource(SslCertificate sslCertificate) throws IOException {
		/*
		File ssl = nginxRepository.configuration().ssl();
		SslCertificate sslCertificateToDelete = load(sslCertificate);
		String sslCertificateHash = sslCertificateToDelete.getResourceIdentifierCertificate().getHash();
		String sslCertificatePrivateKeyHash = sslCertificateToDelete.getResourceIdentifierCertificatePrivateKey()
				.getHash();
		FileUtils.forceDelete(new File(ssl, sslCertificateHash));
		FileUtils.forceDelete(new File(ssl, sslCertificatePrivateKeyHash));
		super.delete(sslCertificateToDelete);
		resourceIdentifierRepository.delete(sslCertificateHash);
		resourceIdentifierRepository.delete(sslCertificatePrivateKeyHash);
		*/
		return OperationType.DELETE;
	}

	@Override
	public OperationResult saveOrUpdate(SslCertificate sslCertificate, InputStream certificateFile,
			InputStream certificatePrivateKeyFile) throws IOException {
		/*
		Nginx nginx = nginxRepository.configuration();
		if (certificateFile != null) {
			if (sslCertificate.getResourceIdentifierCertificate().getId() == null) {
				sslCertificate.setResourceIdentifierCertificate(resourceIdentifierRepository.create());
			}
			IOUtils.copy(certificateFile, new FileOutputStream(
					new File(nginx.ssl(), sslCertificate.getResourceIdentifierCertificate().getHash())));
		}
		if (certificatePrivateKeyFile != null) {
			if (sslCertificate.getResourceIdentifierCertificatePrivateKey().getId() == null) {
				sslCertificate.setResourceIdentifierCertificatePrivateKey(resourceIdentifierRepository.create());
			}
			IOUtils.copy(certificatePrivateKeyFile, new FileOutputStream(
					new File(nginx.ssl(), sslCertificate.getResourceIdentifierCertificatePrivateKey().getHash())));
		}
		*/
		return super.saveOrUpdate(sslCertificate);
	}

	@Override
	public InputStream download(String hash) throws FileNotFoundException {
		//Nginx nginx = nginxRepository.configuration();
		//return new FileInputStream(new File(nginx.ssl(), hash));
		return null;
	}

	@Override
	public List<SslCertificate> listAllFor(Nginx nginx) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SslCertificate> criteriaQuery = criteriaBuilder.createQuery(SslCertificate.class);
		Root<SslCertificate> root = criteriaQuery.from(SslCertificate.class);
		criteriaQuery.where(criteriaBuilder.equal(root.join(SslCertificate_.nginx, JoinType.INNER).get(Nginx_.id), nginx.getId()));
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get(SslCertificate_.commonName)));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}
}
